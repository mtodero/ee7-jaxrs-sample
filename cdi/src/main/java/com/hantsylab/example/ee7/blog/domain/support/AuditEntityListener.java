package com.hantsylab.example.ee7.blog.domain.support;

import com.hantsylab.example.ee7.blog.domain.model.User;
import com.hantsylab.example.ee7.blog.security.AuthenticatedUserLiteral;
import java.time.OffsetDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.spi.CDI;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 *
 * @author hantsy
 */
public class AuditEntityListener {

    private static final Logger LOG = Logger.getLogger(AuditEntityListener.class.getName());

//    @Inject
//    @AuthenticatedUser
//    User user;
//    @PersistenceContext
//    EntityManager em;
    @PrePersist
    public void beforePersist(Object entity) {
        if (entity instanceof AbstractAuditableEntity) {
            AbstractAuditableEntity o = (AbstractAuditableEntity) entity;
            final OffsetDateTime now = OffsetDateTime.now();
            o.setCreatedAt(now);
            o.setUpdatedAt(now);

            if (o.getCreatedBy() == null) {
                o.setCreatedBy(currentUser());
            }
        }
    }

    @PreUpdate
    public void beforeUpdate(Object entity) {
        if (entity instanceof AbstractAuditableEntity) {
            AbstractAuditableEntity o = (AbstractAuditableEntity) entity;
            o.setUpdatedAt(OffsetDateTime.now());

            if (o.getUpdatedBy() == null) {
                o.setUpdatedBy(currentUser());
            }
        }
    }

    private String currentUser() {
        User user = CDI.current().select(User.class, new AuthenticatedUserLiteral()).get();
        LOG.log(Level.FINEST, "get current user form EntityListener@{0}", user);
        if (user == null) {
            return null;
        }
        return user.getUsername();
    }
}
