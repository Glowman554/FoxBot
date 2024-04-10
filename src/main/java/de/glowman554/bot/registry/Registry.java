package de.glowman554.bot.registry;

import java.util.HashMap;

public class Registry<Identifier, Entry> {
    private final HashMap<Identifier, Entry> registry = new HashMap<>();
    private final RegistryProcessor<Identifier, Entry> registryProcessor;

    public Registry(RegistryProcessor<Identifier, Entry> registryProcessor) {
        this.registryProcessor = registryProcessor;
    }

    public void register(Identifier identifier, Entry entry) {
        registryProcessor.register(identifier, entry);
        registry.put(identifier, entry);
    }

    public Entry get(Identifier identifier) {
        Entry entry = registry.get(identifier);
        if (entry == null) {
            throw new IllegalArgumentException("Registry does not contain entry with id " + identifier);
        }
        return entry;
    }

    public void iterate(RegistryIterator<Identifier, Entry> iterator) {
        for (Identifier identifier : registry.keySet()) {
            iterator.process(identifier, registry.get(identifier));
        }
    }

    public boolean has(Identifier identifier) {
        return registry.containsKey(identifier);
    }

    public HashMap<Identifier, Entry> getRegistry() {
        return registry;
    }

    public interface RegistryProcessor<Identifier, Entry> {
        void register(Identifier identifier, Entry entry);
    }

    public interface RegistryIterator<Identifier, Entry> {
        void process(Identifier identifier, Entry entry);
    }
}
