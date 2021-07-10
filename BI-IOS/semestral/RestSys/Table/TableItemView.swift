//
//  TableItemView.swift
//  RestSys
//
//  Created by Thats Me on 20.01.2021.
//

import SwiftUI

struct TableItemView: View {
    
    let table: Table
    let reload: () -> ()
    
    var body: some View {
        
        VStack {
            table.image
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(width: 30)
            
            Text("#" + String(table.id))
                .font(.headline)
        }
        
    }
    
}

struct TableItemView_Previews: PreviewProvider {
    static var previews: some View {
        TableItemView(table: Table.preview, reload: {})
    }
}
