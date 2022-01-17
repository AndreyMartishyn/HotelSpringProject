package ua.martishyn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.martishyn.entities.Message;
import ua.martishyn.repository.MessageRepository;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public List<Message> findByTag(String tag) {
        return messageRepository.findByTag(tag);
    }

    public void save(Message message) {
        messageRepository.save(message);
    }

    public Iterable<Message> findAll() {
        return messageRepository.findAll();
    }
    public void deleteMessageById(Long id){
        messageRepository.deleteById(id);
    }

}
