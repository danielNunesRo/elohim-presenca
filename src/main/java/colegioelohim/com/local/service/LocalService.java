package colegioelohim.com.local.service;

import colegioelohim.com.local.entities.LocalEntity;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class LocalService {

    private static final UUID LOCAL_ID =
            UUID.fromString("11111111-1111-1111-1111-111111111111");

    public LocalEntity getLocalPadrao() {
        return LocalEntity.findById(LOCAL_ID);
    }
}
