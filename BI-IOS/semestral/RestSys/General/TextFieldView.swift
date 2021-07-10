//
//  TextFieldView.swift
//  RestSys
//
//  Created by Thats Me on 16.01.2021.
//

import SwiftUI

struct TextFieldView: View {
    let title: String
    let content: Binding<String>
    let secure: Bool
    
    init (title: String, content: Binding<String>, secure: Bool = false) {
        self.title = title
        self.content = content
        self.secure = secure
    }
    
    var body: some View {
        HStack {
            Text(title)
                .font(.headline)
            
            if secure {
                SecureField(title, text: content)
                    .multilineTextAlignment(.trailing)
            }
            else {
                TextField(title, text: content)
                    .multilineTextAlignment(.trailing)
            }
        }
    }
}

struct TextFieldView_Previews: PreviewProvider {
    static var previews: some View {
        TextFieldView(title: "Placeholder", content: .constant("Test"))
    }
}
