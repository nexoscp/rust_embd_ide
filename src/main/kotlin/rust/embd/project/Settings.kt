package rust.embd.project

import java.util.Collections.emptyList
import java.util.Optional.empty
import java.util.Optional
import java.util.Optional.of

class Settings() {
    var selectedBoard = empty<Board>()
}

enum class Board(val value:String, val chip:Chip, val usb: Optional<ProductID> = empty(), val dependencies: List<Dependency> = emptyList<Dependency>()) {
    Test("test", Chip.Test),
    Example("Example", Chip.nRF52840_xxAA, of(ProductID.Silicon_Labs_CP210x_UART_Bridge))
}
enum class Target(val value:String, vararg val dependencies: Dependency) {
    Test("test"),
    thumbv7em_none_eabi("thumbv7em-none-eabi", Dependency.cortex_m, Dependency.cortex_m_rt, Dependency.rtt_target)
}
enum class Chip(val value:String, target: Target, vararg val dependencies: Dependency){
    Test("test", Target.Test),
    nRF52840_xxAA("nRF52840_xxAA", Target.thumbv7em_none_eabi)
}
enum class Dependency(val value:String, val version:String) {
    cortex_m("cortex-m", "0.6.3"),
    cortex_m_rt("cortex-m-rt", "0.6.12"),
    rtt_target("rtt-target", "0.2.2")
}

enum class VendorID(val value:String, val id: Int) {
    Silicon_Labs("Silicon Labs", 0x10c4)
}
enum class ProductID(val value:String, val vendor: VendorID, val id: Int) {
    Silicon_Labs_CP210x_UART_Bridge("Silicon Labs CP210x UART Bridge", VendorID.Silicon_Labs, 0xea60 )

}
