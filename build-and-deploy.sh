# Input Parameters
EIPAAS_BOX_DIR=../../devon-enterprise/eipaas-box/deploy

# Rebuild PublicRatingsService
cd publicratingsservice
mvn clean package
cd ..

# Rebuild PublicRatingsApplication
cd publicratingsapplication
mvn clean package
cd ..

# --- Copy artifacts into EIPAAS_BOX_DIR
echo "Copying WAR files..."
cp publicratingsservice/target/PublicRatingsService-1.0.0.war $EIPAAS_BOX_DIR/
cp publicratingsapplication/target/PublicRatingsApplication-1.0.0.war $EIPAAS_BOX_DIR/
echo "Done."
