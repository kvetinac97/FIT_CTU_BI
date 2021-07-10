//
//  FoodTab.swift
//  RestSys
//
//  Created by Thats Me on 16.01.2021.
//

import SwiftUI

struct FoodTab: View {
    var body: some View {
        NavigationView {
            FoodView()
        }
        .navigationViewStyle(StackNavigationViewStyle())
        .tabItem {
            Image(systemName: "list.bullet")
            Text("Food list")
        }
    }
}

struct FoodTab_Previews: PreviewProvider {
    static var previews: some View {
        FoodTab()
    }
}
