package uwu.smsgamer.parcore.utils;

import lombok.*;

@Getter
@Setter
public class ThreeEntry<K, V, W> {
    K k;
    V v;
    W w;

    public ThreeEntry(K k0, V v0, W w0) {
        k = k0;
        v = v0;
        w = w0;
    }
}
