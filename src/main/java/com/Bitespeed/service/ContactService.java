package com.Bitespeed.service;

import com.Bitespeed.Repository.ContactRepository;
import com.Bitespeed.dto.ContactData;
import com.Bitespeed.dto.Request.IdentifyRequest;
import com.Bitespeed.dto.Response.IdentifyResponse;
import com.Bitespeed.enity.Contact;
import com.Bitespeed.enity.type.LinkPrecedence;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository repo;

    public IdentifyResponse identify(IdentifyRequest request) {
        List<Contact> matches=repo.findByEmailOrPhoneNumber(request.getEmail(),request.getPhoneNumber());
        if(matches.isEmpty())return createPrimary(request);
        Set<Contact> all=new HashSet<>(matches);
        for(Contact c:matches){
            if(c.getLinkedId()!=null)repo.findById(c.getLinkedId()).ifPresent(all::add);
            all.addAll(repo.findByLinkedId(c.getId()));
        }
        Contact primary=all.stream().filter(c->c.getLinkPrecedence()== LinkPrecedence.PRIMARY).min(Comparator.comparing(Contact::getCreatedAt)).orElseThrow();
        for(Contact c:all){
            if(!c.getId().equals(primary.getId())&&c.getLinkPrecedence()==LinkPrecedence.PRIMARY){
                c.setLinkPrecedence(LinkPrecedence.SECONDARY);
                c.setLinkedId(primary.getId());
                c.setUpdatedAt(LocalDateTime.now());
                repo.save(c);
            }
        }
        boolean exists=all.stream().anyMatch(c-> Objects.equals(c.getEmail(),request.getEmail())&&Objects.equals(c.getPhoneNumber(),request.getPhoneNumber()));
        if(!exists){
            Contact n=new Contact();
            n.setEmail(request.getEmail());
            n.setPhoneNumber(request.getPhoneNumber());
            n.setLinkedId(primary.getId());
            n.setLinkPrecedence(LinkPrecedence.SECONDARY);
            n.setCreatedAt(LocalDateTime.now());
            n.setUpdatedAt(LocalDateTime.now());
            repo.save(n);
            all.add(n);
        }
        return buildResponse(primary,all);
    }

    private IdentifyResponse createPrimary(IdentifyRequest request){
        Contact c=new Contact();
        c.setEmail(request.getEmail());
        c.setPhoneNumber(request.getPhoneNumber());
        c.setLinkPrecedence(LinkPrecedence.PRIMARY);
        c.setCreatedAt(LocalDateTime.now());
        c.setUpdatedAt(LocalDateTime.now());
        repo.save(c);
        return new IdentifyResponse(new ContactData(c.getId(), List.of(c.getEmail()),List.of(c.getPhoneNumber()),List.of()));
    }

    private IdentifyResponse buildResponse(Contact primary,Set<Contact> all){
        List<String> emails=all.stream().map(Contact::getEmail).filter(Objects::nonNull).distinct().sorted((a,b)->a.equals(primary.getEmail())?-1:1).toList();
        List<String> phones=all.stream().map(Contact::getPhoneNumber).filter(Objects::nonNull).distinct().sorted((a,b)->a.equals(primary.getPhoneNumber())?-1:1).toList();
        List<Long> secondaryIds=all.stream().filter(c->c.getLinkPrecedence()==LinkPrecedence.SECONDARY).map(Contact::getId).toList();
        return new IdentifyResponse(new ContactData(primary.getId(),emails,phones,secondaryIds));
    }
}