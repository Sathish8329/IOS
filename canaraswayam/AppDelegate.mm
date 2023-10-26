#import "AppDelegate.h"
#import <Firebase.h>
#import <React/RCTBundleURLProvider.h>
#import "RNSplashScreen.h"
#import <UserNotifications/UserNotifications.h>
#import <FirebaseMessaging/FirebaseMessaging.h>
#import <TrustKit/TrustKit.h>
#import <TrustKit/TSKPinningValidator.h>
#import <TrustKit/TSKPinningValidatorCallback.h>

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    [FIRApp configure];
    self.moduleName = @"canaraswayam";
    // You can add your custom initial props in the dictionary below.
    // They will be passed down to the ViewController used by React Native.
    self.initialProps = @{};

    // Implement SSL Pinning
    [self implementSSLPinning];
    
    // Register for remote notifications
    [self registerForRemoteNotifications];
    
    BOOL didFinishLaunchingWithOptions = [super application:application didFinishLaunchingWithOptions:launchOptions];

    /* Second */
    [RNSplashScreen show];

    /* Third */
    return didFinishLaunchingWithOptions;
}

- (NSURL *)sourceURLForBridge:(RCTBridge *)bridge
{
#if DEBUG
    return [[RCTBundleURLProvider sharedSettings] jsBundleURLForBundleRoot:@"index"];
#else
    return [[NSBundle mainBundle] URLForResource:@"main" withExtension:@"jsbundle"];
#endif
}

// Request user permission for notifications
- (void)registerForRemoteNotifications {
    if (@available(iOS 10.0, *)) {
        UNUserNotificationCenter *center = [UNUserNotificationCenter currentNotificationCenter];
        [center requestAuthorizationWithOptions:(UNAuthorizationOptionBadge | UNAuthorizationOptionSound | UNAuthorizationOptionAlert) completionHandler:^(BOOL granted, NSError * _Nullable error) {
            if (granted) {
                dispatch_async(dispatch_get_main_queue(), ^{
                    [[UIApplication sharedApplication] registerForRemoteNotifications];
                });
            }
        }];
    } else {
        UIUserNotificationType allNotificationTypes = (UIUserNotificationTypeSound | UIUserNotificationTypeAlert | UIUserNotificationTypeBadge);
        UIUserNotificationSettings *settings = [UIUserNotificationSettings settingsForTypes:allNotificationTypes categories:nil];
        [[UIApplication sharedApplication] registerUserNotificationSettings:settings];
    }
}

// Set APNS token for Firebase Messaging
- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken {
    [FIRMessaging messaging].APNSToken = deviceToken;
}

// Implement SSL Pinning
- (void)implementSSLPinning {
     // Override TrustKit's logger method, useful for local debugging
    void (^loggerBlock)(NSString *) = ^void(NSString *message)
    {
        NSLog(@"TrustKit log: %@", message);
    };

    [TrustKit setLoggerBlock:loggerBlock];

    NSDictionary *trustKitConfig =
    @{
        // Swizzling because we can't access the NSURLSession instance used in React Native's fetch method
        kTSKSwizzleNetworkDelegates: @YES,
        kTSKPinnedDomains: @{
            @"hrmsuat.canarabank.in" : @{
                kTSKIncludeSubdomains: @YES, // Pin all subdomains
                kTSKEnforcePinning: @YES, // Block connections if pinning validation failed
                kTSKDisableDefaultReportUri: @YES,
                kTSKPublicKeyHashes : @[
                @"7ZRcD0F0jlszw/MvbS03UcqJfoCbTBLuyWjPdBKeBCo=", 
                @"BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB=", // Fake backup key but we need to provide 2 pins
                ],
            },
        }};
    [TrustKit initSharedInstanceWithConfiguration:trustKitConfig];
    [TrustKit sharedInstance].pinningValidatorCallback = ^(TSKPinningValidatorResult *result, NSString *notedHostname, TKSDomainPinningPolicy *policy) {
        if (result.finalTrustDecision == TSKTrustEvaluationFailedNoMatchingPin) {
        NSLog(@"TrustKit certificate matching failed");
        // Add more logging here. i.e. Sentry, BugSnag etc
        }
    };
}

@end
