//
//  TableDetailViewBottom.swift
//  RestSys
//
//  Created by Thats Me on 24.01.2021.
//

import SwiftUI

struct TableDetailViewBottom: View {
    
    @Binding var pdfDialog: Bool
    let trashOrder: () -> ()
    let submitOrder: () -> ()
    
    var body: some View {
        VStack {
            Spacer()
            HStack {
                Button(action: trashOrder) {
                    Image(systemName: "xmark.circle.fill")
                        .resizable()
                        .frame(width: 40, height: 40)
                }
                Spacer()
                Button(action: { pdfDialog = true }) {
                    Image(systemName: "printer")
                        .resizable()
                        .frame(width: 40, height: 40)
                }
                Spacer()
                Button(action: submitOrder) {
                    Image(systemName: "checkmark.circle.fill")
                        .resizable()
                        .frame(width: 40, height: 40)
                }
            }
            .padding([.leading, .trailing, .bottom], 30)
        }
    }
    
}

struct TableDetailViewBottom_Previews: PreviewProvider {
    static var previews: some View {
        TableDetailViewBottom(pdfDialog: .constant(false), trashOrder: {}, submitOrder: {})
    }
}
