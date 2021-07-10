//
//  AllergensView.swift
//  RestSys
//
//  Created by Thats Me on 30.12.2020.
//

import SwiftUI

struct AllergensView: View {
    
    @StateObject var allergenList = AllergenViewModel()
    
    var body: some View {
        CRUDView<Allergen, List>(viewModel: allergenList, reload: allergenList.loadAllergens) {
            List (allergenList.entities) { allergen in
                AllergenView(allergenItem: allergen)
            }
        }
        .navigationTitle(Text("Allergens"))
    }
}

struct AllergensView_Previews: PreviewProvider {
    static var previews: some View {
        AllergensView()
    }
}
