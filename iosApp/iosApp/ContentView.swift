//
//  ContentView.swift
//  iosApp
//
//  Created by ANDER on 12/2/26.
//

import SwiftUI
  import shared

  struct ContentView: UIViewControllerRepresentable {
      func makeUIViewController(context: Context) -> UIViewController {
          MainViewControllerKt.MainViewController()
      }

      func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
      }
  }
