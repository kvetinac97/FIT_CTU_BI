//
//  MealNameRow.swift
//  CalTrack
//
//  Created by Roman Isaev on 02/05/2021.
//

import SwiftUI

struct MealNameRow: View {
    var meal: Meal
    
    var body: some View {
        VStack {
            Text(meal.name)
                .bold()
                .frame(maxWidth: .infinity, alignment: .center)
                .foregroundColor(.white)
                .font(.system(size: 18))
            Rectangle()
                .stroke(
                    LinearGradient(
                        gradient:
                            Gradient(colors: [.blue, .blue, .white, .red]),
                        startPoint: .leading,
                        endPoint: .trailing),
                    lineWidth: 0.65
                )
                .frame(height: 0.65)
                .padding(.horizontal)
        }
    }
}

struct MealNameRow_Previews: PreviewProvider {
    static var previews: some View {
        MealNameRow(meal: Meal.preview)
    }
}
