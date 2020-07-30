package com.vilgodskiy.modshare.util;

import com.vilgodskiy.modshare.user.domain.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

/**
 * @author Vilgodskiy Sergey 24.07.2020
 *
 * Infirmation about creating and last modification date (Who, When)
 */
@Embeddable
@Getter
public class TracingInfo {

    public TracingInfo() {
        this.creationDate = LocalDateTime.now();
        this.modifyDate = this.creationDate;
    }

    public TracingInfo(User user) {
        this.creationDate = LocalDateTime.now();
        this.modifyDate = this.creationDate;
        if (user != null) {
            creationAuthor = user;
            modifyAuthor = user;
        }
    }

    @Column(name = "d_creation_date", nullable = false)
    private final LocalDateTime creationDate;

    @ManyToOne
    @JoinColumn(name = "uuid_creation_author")
    private User creationAuthor;

    @Column(name = "d_modify_date", nullable = false)
    private LocalDateTime modifyDate;

    @ManyToOne
    @JoinColumn(name = "uuid_modify_author")
    private User modifyAuthor;

    public void modified() {
        this.modifyDate = LocalDateTime.now();
        modifyAuthor = null;
    }

    public void modified(User user) {
        this.modifyDate = LocalDateTime.now();
        this.modifyAuthor = user;
    }
}
