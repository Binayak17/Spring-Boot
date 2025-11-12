package com.test.JournalApp.JournalApp.services;

import com.test.JournalApp.JournalApp.entity.JournalEntry;
import com.test.JournalApp.JournalApp.entity.User;
import com.test.JournalApp.JournalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;
    @Autowired
    private UserService userService;

//    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName){
        User user = userService.findByUsername(userName);
        journalEntry.setDate(LocalDateTime.now());
        JournalEntry saved = journalEntryRepository.save(journalEntry);
        user.getJournalEntries().add(saved);
        userService.saveUser(user);
    }

    public void saveEntry(JournalEntry journalEntry){
        journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAll(){
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id){
        return journalEntryRepository.findById(id);
    }

    public boolean DeleteById(ObjectId id, String userName){
        User user = userService.findByUsername(userName);
        boolean removed = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
        if(removed){
            userService.saveUser(user);
            journalEntryRepository.deleteById(id);
        }
        return removed;
    }

}
