package colegioelohim.com.users.entities;

import colegioelohim.com.users.enums.UserRole;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "tb_users")
public class UserEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @Column(nullable = false)
    public String nome;

    @Column(nullable = false, unique = true)
    public String email;

    @Column(nullable = false)
    public String senhaHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public UserRole role;

    @Column(nullable = false)
    public boolean ativo = true;
}




