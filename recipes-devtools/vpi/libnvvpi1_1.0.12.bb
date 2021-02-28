SUMMARY = "Vision Programming Interface(VPI) is an API for accelerated \
  computer vision and image processing for embedded systems."
HOMEPAGE = "https://developer.nvidia.com/embedded/vpi"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = " \
    file://etc/ld.so.conf.d/vpi1.conf;md5=bfdc8e915558abe115e0fc762a2ff724 \
    file://opt/nvidia/vpi1/include/vpi/VPI.h;endline=48;md5=7c9511c3e53f3d844d189edd66c44682"

inherit l4t_deb_pkgfeed

SRC_COMMON_DEBS = "\
    libnvvpi1_${PV}_arm64.deb;name=lib;subdir=vpi1 \
    vpi1-dev_${PV}_arm64.deb;name=dev;subdir=vpi1 \
"
SRC_URI[lib.sha256sum] = "6cdfc69bd5295a0b3729c0d267a2b7f3794a1274f15c3f0275e0159c26c2cb33"
SRC_URI[dev.sha256sum] = "392b1aec02aa5bab8106077fa6f048f8e44c7af9b639140370cb11446a74187b"

SRC_URI_append = " file://0001-vpi-config-allow-to-compute-the-installation-prefix.patch"

S = "${WORKDIR}/vpi1"
B = "${S}"

DEPENDS = "cuda-cudart cuda-cufft tegra-libraries"
SYSROOT_DIRS_append = " /opt"

COMPATIBLE_MACHINE = "(tegra)"

do_compile() {
    :
}

do_install() {
    install -d ${D}/opt/nvidia/vpi1/lib64
    cp -R --preserve=mode,timestamps,links --no-dereference ${B}/opt/nvidia/vpi1/lib64/* ${D}/opt/nvidia/vpi1/lib64/
    install -d ${D}/${sysconfdir}/ld.so.conf.d/
    cp --preserve=mode,timestamps ${B}/etc/ld.so.conf.d/vpi1.conf ${D}/${sysconfdir}/ld.so.conf.d/
    install -d  ${D}/opt/nvidia/vpi1/include
    cp -R --preserve=mode,timestamps ${B}/opt/nvidia/vpi1/include/* ${D}/opt/nvidia/vpi1/include/
    install -d ${D}${datadir}
    cp -R --preserve=mode,timestamps ${B}/usr/share/vpi1 ${D}${datadir}/
}

INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_SYSROOT_STRIP = "1"

PACKAGES = "${PN} ${PN}-dev"
FILES_${PN} = "/opt/nvidia/vpi1/lib64/libnvvpi.so.1* ${sysconfdir}/ld.so.conf.d"
FILES_${PN}-dev = "/opt/nvidia/vpi1/lib64/libnvvpi.so /opt/nvidia/vpi1/include ${datadir}/vpi1/cmake"
PACKAGE_ARCH = "${TEGRA_PKGARCH}"