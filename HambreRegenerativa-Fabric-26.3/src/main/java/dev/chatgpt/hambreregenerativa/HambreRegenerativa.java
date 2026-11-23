package dev.chatgpt.hambreregenerativa;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.MinecraftServer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class HambreRegenerativa implements ModInitializer {
    /*
     * El juego funciona normalmente a 20 ticks por segundo.
     * Cada intervalo restaura 2 puntos de salud = 1 corazón completo.
     */
    private static final Map<UUID, Integer> TICKS_ACUMULADOS = new HashMap<>();

    @Override
    public void onInitialize() {
        // A cero de hambre, la inanición vanilla haría más daño que la curación de 60 s.
        // Se cancela únicamente el daño STARVE para que la última fila de la tabla sea jugable.
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (entity instanceof ServerPlayer && source.is(DamageTypes.STARVE)) {
                return false;
            }
            return true;
        });

        ServerTickEvents.END_SERVER_TICK.register(HambreRegenerativa::procesarJugadores);
    }

    private static void procesarJugadores(MinecraftServer server) {
        Set<UUID> conectados = new HashSet<>();

        for (ServerPlayer jugador : server.getPlayerList().getPlayers()) {
            UUID id = jugador.getUUID();
            conectados.add(id);

            // No intervenir en jugadores muertos, creativos o espectadores.
            if (!jugador.isAlive()
                    || jugador.getAbilities().instabuild
                    || jugador.isSpectator()) {
                TICKS_ACUMULADOS.remove(id);
                continue;
            }

            // Si ya tiene toda la vida, el contador vuelve a cero.
            if (jugador.getHealth() >= jugador.getMaxHealth()) {
                TICKS_ACUMULADOS.remove(id);
                continue;
            }

            int hambre = jugador.getFoodData().getFoodLevel();
            int intervalo = intervaloEnTicks(hambre);
            int transcurridos = TICKS_ACUMULADOS.getOrDefault(id, 0) + 1;

            if (transcurridos >= intervalo) {
                jugador.heal(2.0F); // 1 corazón
                transcurridos = 0;
            }

            TICKS_ACUMULADOS.put(id, transcurridos);
        }

        // Limpiar los contadores de quienes se desconectaron.
        TICKS_ACUMULADOS.keySet().retainAll(conectados);
    }

    private static int intervaloEnTicks(int hambre) {
        if (hambre >= 18) return  40;  // 9-10 muslitos: 2 segundos
        if (hambre >= 14) return 100;  // 7-8 muslitos: 5 segundos
        if (hambre >= 10) return 200;  // 5-6 muslitos: 10 segundos
        if (hambre >=  6) return 400;  // 3-4 muslitos: 20 segundos
        if (hambre >=  2) return 800;  // 1-2 muslitos: 40 segundos
        return 1200;                   // 0 muslitos: 60 segundos
    }
}
