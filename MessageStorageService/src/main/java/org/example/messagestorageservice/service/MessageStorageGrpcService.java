package org.example.messagestorageservice.service;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import org.example.messagestorage.grpc.*;
import org.example.messagestorageservice.entity.ChatEntity;
import org.example.messagestorageservice.entity.ChatParticipantEntity;
import org.example.messagestorageservice.entity.MessageEntity;
import org.example.messagestorageservice.repository.ChatParticipantRepository;
import org.example.messagestorageservice.repository.ChatRepository;
import org.example.messagestorageservice.repository.MessageRepository;

import java.time.Instant;
import java.util.List;

@GrpcService
public class MessageStorageGrpcService extends MessageStorageServiceGrpc.MessageStorageServiceImplBase {

    private final ChatRepository chatRepository;
    private final ChatParticipantRepository participantRepository;
    private final MessageRepository messageRepository;

    public MessageStorageGrpcService(
            ChatRepository chatRepository,
            ChatParticipantRepository participantRepository,
            MessageRepository messageRepository
    ) {
        this.chatRepository = chatRepository;
        this.participantRepository = participantRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public void createChat(CreateChatRequest req, StreamObserver<CreateChatResponse> obs) {

        ChatEntity chat = new ChatEntity();
        chat.setTitle(req.getTitle());
        chat.setType(req.getType());
        chat = chatRepository.save(chat);

        for (String userId : req.getUserIdsList()) {
            ChatParticipantEntity p = new ChatParticipantEntity();
            p.setChatId(chat.getId());
            p.setUserId(userId);
            participantRepository.save(p);
        }

        CreateChatResponse res = CreateChatResponse.newBuilder()
                .setChatId(chat.getId())
                .build();

        obs.onNext(res);
        obs.onCompleted();
    }

    @Override
    public void getChatsForUser(GetChatsRequest req, StreamObserver<GetChatsResponse> obs) {
        List<ChatParticipantEntity> parts = participantRepository.findByUserId(req.getUserId());

        GetChatsResponse.Builder builder = GetChatsResponse.newBuilder();

        for (ChatParticipantEntity p : parts) {

            ChatEntity chat = chatRepository.findById(p.getChatId()).orElse(null);
            if (chat == null) continue;

            List<ChatParticipantEntity> all = participantRepository.findByChatId(chat.getId());

            ChatItem item = ChatItem.newBuilder()
                    .setChatId(chat.getId())
                    .setTitle(chat.getTitle())
                    .setType(chat.getType())
                    .addAllParticipants(all.stream().map(ChatParticipantEntity::getUserId).toList())
                    .build();

            builder.addChats(item);
        }

        obs.onNext(builder.build());
        obs.onCompleted();
    }

    @Override
    public void getChatMessages(GetMessagesRequest req, StreamObserver<GetMessagesResponse> obs) {
        List<MessageEntity> msgs = messageRepository.findByChatIdOrderByCreatedAtAsc(req.getChatId());

        GetMessagesResponse.Builder b = GetMessagesResponse.newBuilder();

        for (MessageEntity m : msgs) {
            b.addMessages(
                    MessageItem.newBuilder()
                            .setId(m.getId())
                            .setFromUserId(m.getFromUserId())
                            .setBody(m.getBodyJson())
                            .setCreatedAt(m.getCreatedAt().toString())
            );
        }

        obs.onNext(b.build());
        obs.onCompleted();
    }

    @Override
    public void saveMessage(SaveMessageRequest req, StreamObserver<SaveMessageResponse> obs) {

        MessageEntity m = new MessageEntity();
        m.setChatId(req.getChatId());
        m.setFromUserId(req.getFromUserId());
        m.setToUserIds(String.join(",", req.getToUserIdsList()));
        m.setBodyJson("{\"type\":\"" + req.getBody().getType() + "\",\"text\":\"" + req.getBody().getText() + "\"}");
        m.setCreatedAt(Instant.now());

        m = messageRepository.save(m);

        SaveMessageResponse res = SaveMessageResponse.newBuilder()
                .setMessageId(m.getId())
                .setCreatedAt(m.getCreatedAt().toString())
                .build();

        obs.onNext(res);
        obs.onCompleted();
    }
}


