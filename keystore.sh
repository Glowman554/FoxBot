set -ex

DOMAIN=foxbot.toxicfox.de
read -p "Keystore password > " PASSWORD

sudo openssl pkcs12 -export \
	-in /etc/letsencrypt/live/$DOMAIN/fullchain.pem \
	-inkey /etc/letsencrypt/live/$DOMAIN/privkey.pem \
	-out /tmp/$DOMAIN.p12 \
	-name $DOMAIN \
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
