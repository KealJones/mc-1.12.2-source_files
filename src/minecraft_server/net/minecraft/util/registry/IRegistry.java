package net.minecraft.util.registry;

public interface IRegistry<K, V> extends Iterable<V>
{
    /**
     * Register an object on this registry.
     */
    void putObject(K key, V value);
}
