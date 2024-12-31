package com.viajes.viajesCompartidos.services;

import com.viajes.viajesCompartidos.DTO.chat.ChatDTO;
import com.viajes.viajesCompartidos.DTO.chat.InputMessageDTO;
import com.viajes.viajesCompartidos.DTO.chat.MessageDTO;
import com.viajes.viajesCompartidos.entities.Chat;
import com.viajes.viajesCompartidos.entities.Message;
import com.viajes.viajesCompartidos.entities.User;
import com.viajes.viajesCompartidos.exceptions.users.UserNotFoundException;
import com.viajes.viajesCompartidos.repositories.ChatRepository;
import com.viajes.viajesCompartidos.repositories.MessageRepository;
import com.viajes.viajesCompartidos.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    @Autowired
    public ChatService(ChatRepository chatRepository, UserRepository userRepository, MessageRepository messageRepository) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }
    public ChatDTO getChat(int tripId) {
        Chat chat = chatRepository.findByTripId(tripId).orElse(null);
        if (chat == null) {
            return null;
        }
        return new ChatDTO(chat);

    }
    @Transactional
    public MessageDTO sendMessage(InputMessageDTO messageDTO) {
        Message message = new Message();
        Chat chat = chatRepository.findById(messageDTO.getChatId()).orElse(null);
        User user = userRepository.findById(messageDTO.getUserId()).orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        message.setContent(messageDTO.getContent());
        message.setChat(chat);
        message.setUser(user);
        assert chat != null;
        messageRepository.save(message);
        chat.addMessage(message);
        chatRepository.save(chat);
        return new MessageDTO(message);
    }
}
