import SwiftUI
import shared

@main
struct PasapoteApp: App {
    init() {
        IosModuleKt.initKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
                .ignoresSafeArea(.all)
        }
    }
}
