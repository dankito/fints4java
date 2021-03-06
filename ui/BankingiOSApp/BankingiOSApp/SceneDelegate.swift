import UIKit
import SwiftUI
import CoreData
import BankingUiSwift


class SceneDelegate: UIResponder, UIWindowSceneDelegate {

    var window: UIWindow?
    
    var persistence: CoreDataBankingPersistence?


    func scene(_ scene: UIScene, willConnectTo session: UISceneSession, options connectionOptions: UIScene.ConnectionOptions) {
        // Use this method to optionally configure and attach the UIWindow `window` to the provided UIWindowScene `scene`.
        // This delegate does not imply the connecting scene or session are new (see `application:configurationForConnectingSceneSession` instead).
        
        let authenticationService = setupBankingUi()

        if let windowScene = scene as? UIWindowScene {
            let window = UIWindow(windowScene: windowScene)
            self.window = window
            
            if authenticationService.needsAuthenticationToUnlockApp {
                window.rootViewController = UIHostingController(rootView: LoginDialog(authenticationService) { _ in
                    self.showApplicationMainView(window: window)
                } )
            }
            else {
                showApplicationMainView(window: window)
            }
            
            window.makeKeyAndVisible()
        }
    }
    
    private func setupBankingUi() -> AuthenticationService {
        let persistence = CoreDataBankingPersistence()
        let authenticationService = AuthenticationService(persistence)
        self.persistence = persistence
        
        DependencyInjector.register(dependency: authenticationService)
        
        return authenticationService
    }
    
    
    func showApplicationMainView() {
        if let window = window {
            showApplicationMainView(window: window)
        }
    }
    
    private func showApplicationMainView(window: UIWindow) {
        if let persistence = persistence {
            let appDataFolder = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true).first
                ?? Bundle.main.resourceURL?.absoluteString ?? ""
            
            let dataFolder = URL(fileURLWithPath: "data", isDirectory: true, relativeTo: URL(fileURLWithPath: appDataFolder))
            
            let presenter = BankingPresenterSwift(dataFolder: dataFolder, router: SwiftUiRouter(), webClient: UrlSessionWebClient(), persistence: persistence, transactionPartySearcher: persistence, bankIconFinder: SwiftBankIconFinder(), serializer: NoOpSerializer(), asyncRunner: DispatchQueueAsyncRunner())

            DependencyInjector.register(dependency: presenter)
            
            window.rootViewController = UINavigationController(rootViewController: TabBarController(presenter))
        }
    }
    

    func sceneDidDisconnect(_ scene: UIScene) {
        // Called as the scene is being released by the system.
        // This occurs shortly after the scene enters the background, or when its session is discarded.
        // Release any resources associated with this scene that can be re-created the next time the scene connects.
        // The scene may re-connect later, as its session was not neccessarily discarded (see `application:didDiscardSceneSessions` instead).
    }

    func sceneDidBecomeActive(_ scene: UIScene) {
        // Called when the scene has moved from an inactive state to an active state.
        // Use this method to restart any tasks that were paused (or not yet started) when the scene was inactive.
    }

    func sceneWillResignActive(_ scene: UIScene) {
        // Called when the scene will move from an active state to an inactive state.
        // This may occur due to temporary interruptions (ex. an incoming phone call).
    }

    func sceneWillEnterForeground(_ scene: UIScene) {
        // Called as the scene transitions from the background to the foreground.
        // Use this method to undo the changes made on entering the background.
    }

    func sceneDidEnterBackground(_ scene: UIScene) {
        // Called as the scene transitions from the foreground to the background.
        // Use this method to save data, release shared resources, and store enough scene-specific state information
        // to restore the scene back to its current state.

        // Save changes in the application's managed object context when the application transitions to the background.
        persistence?.saveContext()
    }


}

