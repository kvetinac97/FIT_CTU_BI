//
//  ContentView.swift
//  RestSys
//
//  Created by Thats Me on 26.12.2020.
//

import SwiftUI

struct ContentView: View {
    
    @EnvironmentObject var user: AuthUser
    
    var body: some View {
        if user.loggedIn {
            TabView {
                TableTab()
                
                FoodTab()
                
                KitchenTab()
            }
        }
        else {
            TabView {
                FoodTab()
                LoginTab()
            }
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
