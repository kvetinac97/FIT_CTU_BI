//
//  PlusButton.swift
//  CalTrack
//
//  Created by Roman Isaev on 02/05/2021.
//

import SwiftUI

struct PlusButton: View {
    let action: () -> ()
    @Binding var isFoodTabShown: Bool
    @State   var searchActivityPresented = false
    
    var body: some View {
        Button (action: { searchActivityPresented = true }) {
            Image(systemName: "plus.circle")
                .resizable()
                .frame(width: 24, height: 24)
                .foregroundColor(.white)
        }
        .overlay(
            Circle()
                .stroke(
                    LinearGradient(
                        gradient:
                            Gradient(
                                colors: searchActivityPresented
                                ? [.red, .white, .blue, .red]
                                : [.white,.blue, .blue]
                            ),
                        startPoint: .topLeading,
                        endPoint: .bottomLeading),
                    lineWidth: 2)
                .frame(width: 23, height: 23) )
        .sheet(isPresented: $searchActivityPresented) {
            if isFoodTabShown {
                FoodSheet(presented: $searchActivityPresented, action: action)
            }
            else {
                ActivitySheet(presented: $searchActivityPresented, action: action)
            }
        }
    }
}
