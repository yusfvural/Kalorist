package com.yusufvural.kaloritakip.ui.auth.login

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.yusufvural.kaloritakip.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class LoginScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun login_fields_are_displayed() {
        // Wait for Splash Screen delay (1 second) + navigation
        composeTestRule.waitForIdle()
        Thread.sleep(2000) 

        // Check if we are already logged in (Dashboard visible)
        // We look for a bottom navigation item or a dashboard element.
        // The ModernFloatingNavigationBar has an item with route "profile".
        // Use a known text or description. In navigation.kt, icons are used.
        // We can look for text unique to Dashboard or Profile.
        
        // Try to find "Giriş Yap". If not found, assume we are logged in.
        try {
            composeTestRule.onNodeWithText("Giriş Yap").assertExists()
        } catch (e: AssertionError) {
            // "Giriş Yap" not found, likely logged in.
            // Navigate to Profile and Logout.
            
            // Note: Accessibility content description might be null in navigation.kt, 
            // but we can try to click on the coordinates or add a test tag. 
            // For now, let's assume we can find the "Profil" text if it existed, but likely it's just an icon.
            // Let's use the contentDescription if available, or try to find a known text on Dashboard like "Kalori Takip".
            
            // Assuming we are on Dashboard.
            // We need to click the Profile icon. 
            // Since navigation.kt uses icons without text labels, finding it by text is hard.
            // Let's try to find the "Çıkış Yap" button directly if we can navigate to Profile? 
            // No, we are on Dashboard.
            
            // WORKAROUND: In a real test we'd use TestTags. 
            // Since I can't easily add tags to the code right now without recompiling app, 
            // I will use a clearer approach:
            // Use FirebaseAuth to sign out programmatically in the test, then restart activity?
            // Or simpler: Just fail with a clear message? 
            // No, user wants it to work.
            
            // Let's use the programmatic sign out!
            // We need to inject FirebaseAuth.
            val auth = com.google.firebase.auth.FirebaseAuth.getInstance()
            if (auth.currentUser != null) {
                auth.signOut()
                // Restart Activity to trigger Splash -> Login flow
                composeTestRule.activityRule.scenario.recreate()
                Thread.sleep(2000)
            }
        }
        
        // Now we should be on Login Screen
        composeTestRule.onNodeWithText("Giriş Yap").assertExists()
        composeTestRule.onNodeWithText("E-posta").assertExists()
        composeTestRule.onNodeWithText("Şifre").assertExists()
    }
}
