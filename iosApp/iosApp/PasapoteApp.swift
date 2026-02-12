import SwiftUI
import shared

@main
struct PasapoteApp: App {
    init() {
        IosModuleKt.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
                .ignoresSafeArea(.all)
        }
    }
}
