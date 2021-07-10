//
//  AllergenView.swift
//  RestSys
//
//  Created by Thats Me on 30.12.2020.
//

import SwiftUI

struct AllergenView: View {
    
    let allergenItem: Allergen
    
    var body: some View {
        HStack() {
            Text(String(allergenItem.id))
                .font(.headline)
            Spacer()
            Text(allergenItem.name)
        }
        .padding([.leading, .trailing])
    }
}

struct AllergenView_Previews: PreviewProvider {
    static var previews: some View {
        AllergenView (
            allergenItem: Allergen.preview
        )
    }
}
