package fr.fiestacockpit.diag

import android.service.notification.NotificationListenerService

/**
 * Service volontairement vide. Android n'autorise MediaSessionManager.getActiveSessions()
 * qu'à une application dont un NotificationListenerService est activé par l'utilisateur
 * (Réglages > Accès aux notifications). Aucune notification n'est lue ni stockée.
 */
class DiagListenerService : NotificationListenerService()
