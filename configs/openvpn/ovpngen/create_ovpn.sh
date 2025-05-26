#!/bin/bash

EASYRSA_DIR="$HOME/easy-rsa"
cd "$EASYRSA_DIR" || { echo "❌ Не удалось перейти в $EASYRSA_DIR"; exit 1; }

CLIENT_NAME=$1
if [ -z "$CLIENT_NAME" ]; then
    read -p "Введите имя клиента: " CLIENT_NAME
fi

./easyrsa gen-req "$CLIENT_NAME" nopass || { echo "❌ Ошибка при создании запроса"; exit 1; }
./easyrsa sign-req client "$CLIENT_NAME" <<EOF
yes
EOF

CA_CERT="$EASYRSA_DIR/pki/ca.crt"
CLIENT_CERT="$EASYRSA_DIR/pki/issued/${CLIENT_NAME}.crt"
CLIENT_KEY="$EASYRSA_DIR/pki/private/${CLIENT_NAME}.key"
TA_KEY="$EASYRSA_DIR/ta.key"

for file in "$CA_CERT" "$CLIENT_CERT" "$CLIENT_KEY" "$TA_KEY"; do
    if [ ! -r "$file" ]; then
        echo "❌ Файл $file не найден или недоступен"
        exit 1
    fi
done

SERVER_IP=$(curl -s ifconfig.me)

OUTPUT_DIR="$EASYRSA_DIR/ovpngen"
OUTPUT_FILE="$OUTPUT_DIR/${CLIENT_NAME}.ovpn"


cat > "$OUTPUT_FILE" << EOF
client
dev tun
proto udp
remote $SERVER_IP 1194
resolv-retry infinite
nobind
remote-cert-tls server
persist-key
persist-tun

<ca>
$(cat "$CA_CERT")
</ca>

<cert>
$(cat "$CLIENT_CERT")
</cert>

<key>
$(cat "$CLIENT_KEY")
</key>

<tls-auth>
$(sudo cat "$TA_KEY")
</tls-auth>

key-direction 1
verb 3
EOF

echo "✅ Файл $OUTPUT_FILE был создан с встроенными сертификатами и ключами."
