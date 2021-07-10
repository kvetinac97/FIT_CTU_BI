//
//  KitchenTab.swift
//  RestSys
//
//  Created by Thats Me on 22.01.2021.
//

import SwiftUI

struct KitchenTab: View {
    
    var body: some View {
        NavigationView {
            KitchenView()
        }
        .navigationViewStyle(StackNavigationViewStyle())
        .tabItem {
            Image(systemName: "house")
            Text("Kitchen")
        }
    }
    
}

struct KitchenTab_Previews: PreviewProvider {
    static var previews: some View {
        KitchenTab()
    }
}
