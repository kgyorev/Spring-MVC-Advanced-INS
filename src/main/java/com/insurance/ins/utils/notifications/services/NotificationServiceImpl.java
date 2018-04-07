package com.insurance.ins.utils.notifications.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service()
public class NotificationServiceImpl implements NotificationService {

    public static final String NOTIFY_MSG_SESSION_KEY = "siteNotificationMessages";

    private final HttpSession httpSession;

    @Autowired
    public NotificationServiceImpl(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    @Override
    public void addInfoMessage(String msg) {
        addNotificationMessage(NotificationMessageType.SUCCESS, msg);
    }

    @Override
    public void addErrorMessage(String msg) {
        addNotificationMessage(NotificationMessageType.DANGER, msg);
    }
    @Override
    public void addWarningMessage(String msg) {
        addNotificationMessage(NotificationMessageType.WARNING, msg);
    }

    private void addNotificationMessage(NotificationMessageType type, String msg) {
        List<NotificationMessage> notifyMessages = (List<NotificationMessage>)
                httpSession.getAttribute(NOTIFY_MSG_SESSION_KEY);
        if (notifyMessages == null) {
            notifyMessages = new ArrayList<NotificationMessage>();
        }
        notifyMessages.add(new NotificationMessage(type, msg));
        httpSession.setAttribute(NOTIFY_MSG_SESSION_KEY, notifyMessages);
    }

    public enum NotificationMessageType {
        SUCCESS,
        WARNING,
        DANGER
    }

    public class NotificationMessage {
        NotificationMessageType type;
        String text;

        public NotificationMessage(NotificationMessageType type, String text) {
            this.type = type;
            this.text = text;
        }

        public NotificationMessageType getType() {
            return type;
        }

        public String getText() {
            return text;
        }
    }
}
