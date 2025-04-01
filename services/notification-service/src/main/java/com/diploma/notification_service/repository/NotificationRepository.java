package com.diploma.notification_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.diploma.notification_service.models.Notification;

public interface NotificationRepository extends MongoRepository<Notification, String> {
    
}
