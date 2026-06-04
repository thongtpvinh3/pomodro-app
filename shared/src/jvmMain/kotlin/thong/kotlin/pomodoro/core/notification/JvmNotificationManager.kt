package thong.kotlin.pomodoro.core.notification

import java.awt.SystemTray
import java.awt.TrayIcon
import java.awt.Image
import java.awt.image.BufferedImage

class JvmNotificationManager : NotificationManager {
    private var trayIcon: TrayIcon? = null

    init {
        if (SystemTray.isSupported()) {
            try {
                val tray = SystemTray.getSystemTray()
                // Create a 1x1 transparent image as a placeholder icon
                // A valid image is often required for the TrayIcon to be added successfully
                val image: Image = BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)
                
                trayIcon = TrayIcon(image, "Pomodoro Timer")
                trayIcon?.isImageAutoSize = true
                tray.add(trayIcon)
            } catch (e: Exception) {
                println("Failed to initialize SystemTray: ${e.message}")
            }
        } else {
            println("SystemTray is not supported on this platform.")
        }
    }

    override fun showNotification(title: String, message: String) {
        val icon = trayIcon
        if (icon != null) {
            // displayMessage might fail if the OS notification system is disabled or busy
            try {
                icon.displayMessage(title, message, TrayIcon.MessageType.INFO)
            } catch (e: Exception) {
                println("Failed to display notification: ${e.message}")
                showFallbackNotification(title, message)
            }
        } else {
            showFallbackNotification(title, message)
        }
    }

    private fun showFallbackNotification(title: String, message: String) {
        println("NOTIFICATION (Fallback): $title - $message")
        // In a real app, you might show a small Compose dialog here
    }

    override fun requestPermission() {
        // Desktop typically doesn't need runtime permission request like Android
    }
}
