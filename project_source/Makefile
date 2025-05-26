CC = riscv64-linux-gnu-gcc
CFLAGS = -static -O2
TARGET = main
BUILD_DIR = build
PKG_NAME = $(TARGET)-riscv.deb
PKG_DIR = debian

all: $(TARGET)

$(TARGET): main.c
	$(CC) $(CFLAGS) -o $(TARGET) main.c

install: $(TARGET)
	mkdir -p $(PKG_DIR)/usr/local/bin
	cp $(TARGET) $(PKG_DIR)/usr/local/bin/$(TARGET)
	chmod 755 $(PKG_DIR)/usr/local/bin/$(TARGET)

package: install
	dpkg-deb --build $(PKG_DIR) $(PKG_NAME)

packages: package
	mkdir -p $(BUILD_DIR)
	cp $(PKG_NAME) $(BUILD_DIR)/
	cd $(BUILD_DIR) && dpkg-scanpackages . /dev/null | gzip -9c > Packages.gz

clean:
	rm -f $(TARGET)
	rm -f $(PKG_NAME)
	rm -rf $(PKG_DIR)/usr/local/bin
	rm -rf $(BUILD_DIR)