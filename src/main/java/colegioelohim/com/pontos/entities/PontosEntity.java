package colegioelohim.com.pontos.entities;

import colegioelohim.com.local.entities.LocalEntity;
import colegioelohim.com.users.entities.UserEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_pontos")
public class PontosEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    public UserEntity usuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "local_id")
    public LocalEntity local;

    @Column(nullable = false)
    public LocalDateTime dataHora;

    @Column(nullable = false)
    public double latitude;

    @Column(nullable = false)
    public double longitude;

    @Column(nullable = false)
    public boolean valido;

    @Column
    public String motivoInvalidacao;

}
