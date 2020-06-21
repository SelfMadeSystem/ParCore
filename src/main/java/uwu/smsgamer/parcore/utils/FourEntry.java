package uwu.smsgamer.parcore.utils;

import lombok.*;

@Getter
@Setter
public class FourEntry<K, V, W, X> {
    K k;
    V v;
    W w;
    X x;

    public FourEntry(K k0, V v0, W w0, X x0) {
        k = k0;
        v = v0;
        w = w0;
        x = x0;
    }
}
