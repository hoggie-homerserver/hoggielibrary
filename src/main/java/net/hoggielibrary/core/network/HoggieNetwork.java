package net.hoggielibrary.core.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public final class HoggieNetwork {

    public static <T extends CustomPayload> PacketType<T> registerPacket(
            Identifier id, PacketCodec<? super RegistryByteBuf, T> codec) {
        return PacketType.create(id, codec);
    }

    public static <T extends CustomPayload> void registerClientReceiver(
            PacketType<T> type, ClientPlayNetworking.PlayPayloadHandler<T> handler) {
        ClientPlayNetworking.registerReceiver(type.id(), handler);
    }

    public static <T extends CustomPayload> void registerServerReceiver(
            PacketType<T> type, ServerPlayNetworking.PlayPayloadHandler<T> handler) {
        ServerPlayNetworking.registerGlobalReceiver(type.id(), handler);
    }

    public static <T extends CustomPayload> void sendToServer(T packet) {
        ClientPlayNetworking.send(packet);
    }

    public record PacketType<T extends CustomPayload>(
            CustomPayload.Id<T> id,
            PacketCodec<? super RegistryByteBuf, T> codec) {

        public static <T extends CustomPayload> PacketType<T> create(
                Identifier id, PacketCodec<? super RegistryByteBuf, T> codec) {
            return new PacketType<>(new CustomPayload.Id<>(id), codec);
        }

        public void registerCodecs() {
            PayloadTypeRegistry.playC2S().register(this.id, this.codec);
            PayloadTypeRegistry.playS2C().register(this.id, this.codec);
        }
    }
}
