//
//  LoginTab.swift
//  RestSys
//
//  Created by Thats Me on 24.01.2021.
//

import SwiftUI

struct LoginTab: View {
    var body: some View {
        NavigationView {
            LoginView()
                .navigationTitle("Login")
        }
        .tabItem {
            Image(systemName: "lock")
            Text("Login")
        }
    }
}

struct LoginTab_Previews: PreviewProvider {
    static var previews: some View {
        LoginTab()
    }
}
