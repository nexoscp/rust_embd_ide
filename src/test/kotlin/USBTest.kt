import org.junit.Test
import org.usb4java.javax.adapter.UsbServicesAdapter
import rust.embd.project.Board
import java.util.*
import javax.usb.UsbDevice
import javax.usb.UsbHostManager
import javax.usb.UsbHub
import javax.usb.event.UsbServicesEvent

class USBTest {
    @Test
    fun test() {

        val boards = Board.values().filter { it.usb.isPresent }.map { it.usb.get() to it} .toMap()
        boards.entries.forEach { println("" + it.key.vendor.id.toString(16) + ":" + it.key.id.toString(16) + " " + it.value.chip.value) }
        val host = UsbHostManager.getUsbServices()
        host.addUsbServicesListener( object: UsbServicesAdapter() {
            override fun usbDeviceAttached(event: UsbServicesEvent?) { }

            override fun usbDeviceDetached(event: UsbServicesEvent?) { }
        })
        traverse(host.rootUsbHub)
    }

    private fun traverse(hub:UsbHub) {
        (hub.attachedUsbDevices)
                .forEach { device ->
            if (device is UsbHub) {
                traverse(device)
            } else if(device is UsbDevice) {
                val description = device.usbDeviceDescriptor
                val vendorID = description.idVendor()
                val productID = description.idProduct()
                print("$vendorID:$productID\n")
            }
        }
    }
}
