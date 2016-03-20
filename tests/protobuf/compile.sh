SRC_DIR=$(dirname "$0")
protoc -I=$SRC_DIR --java_out=$SRC_DIR/java $SRC_DIR/addressbook_proto2.proto
protoc -I=$SRC_DIR --java_out=$SRC_DIR/java $SRC_DIR/addressbook_proto3.proto

pushd $SRC_DIR/java
javac -cp .:./lib/protobuf-java-3.0.0-beta-2.jar ./program/Main.java
popd
