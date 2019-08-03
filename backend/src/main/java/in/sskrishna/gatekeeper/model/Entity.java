package in.sskrishna.gatekeeper.model;

import lombok.Data;
import org.springframework.data.annotation.Version;

@Data
public class Entity {
    protected String id;
    @Version
    protected long version;
}
