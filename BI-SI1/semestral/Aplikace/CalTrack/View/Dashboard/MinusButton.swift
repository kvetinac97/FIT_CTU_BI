//
//  MinusButton.swift
//  CalTrack
//
//  Created by Roman Isaev on 01/05/2021.
//

import SwiftUI

struct MinusButton: View {
    @Binding var isDeletePresented: Bool
    
    var body: some View {
        Button (action: { isDeletePresented.toggle() }) {
            Image(systemName: "minus.circle")
                .resizable()
                .frame(width: 24, height: 24)
                .foregroundColor(.white)
        }
        .overlay(
            Circle()
                .stroke(
                    LinearGradient(
                        gradient: Gradient(colors: isDeletePresented ? [.red, .white, .blue, .red] : [.white,.blue, .blue]),
                        startPoint: isDeletePresented ? .leading : .topLeading,
                        endPoint: isDeletePresented ? .trailing : .bottomLeading
                    ),
                    lineWidth: 2
                )
                .frame(width: 23, height: 23)
        )
    }
}
