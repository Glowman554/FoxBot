set -ex

DOMAIN=foxbot.glowman554.de
read -p "Keystore password > " PASSWORD

sudo openssl pkcs12 -export \
	-in /etc/letsencrypt/live/$DOMAIN/cert.pem \
	-inkey /etc/letsencrypt/live/$DOMAIN/privkey.pem \
	-out /tmp/$DOMAIN.p12 \
	-name $DOMAIN \
	-CAfile /etc/letsencrypt/live/$DOMAIN/fullchain.pem \
	-caname "Let's Encrypt Authority X3" \
	-password pass:$PASSWORD

sudo keytool -importkeystore \
	-deststorepass $PASSWORD \
	-destkeypass $PASSWORD \
	-deststoretype pkcs12 \
	-srckeystore /tmp/$DOMAIN.p12 \
	-srcstoretype PKCS12 \
	-srcstorepass $PASSWORD \
	-destkeystore .keystore \
	-alias $DOMAIN

# use "certbot certonly" to get