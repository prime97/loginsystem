package com.login.logintutorial.registration.token;

import com.login.logintutorial.appuser.AppUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Component
public class ConfirmationToken {

    @SequenceGenerator(name="confirmation_token_sequence",
            sequenceName = "confirmation_token_sequence",
            allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "confirmation_token_sequence")

    private Long id;
    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private LocalDateTime confirmedAt;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "app_user_id"
    )
    private AppUser appUser;


    public ConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expiresAt, AppUser appUser) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;

        this.appUser = appUser;
    }


}
