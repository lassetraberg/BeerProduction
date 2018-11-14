import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SubheaderComponent } from './subheader/subheader.component';
import {MatSnackBarModule, MatListModule, MatToolbarModule, MatDividerModule, MatButtonModule, MatTabsModule, MatTableModule, MatFormFieldModule, MatInputModule, MatSelectModule, MatCardModule} from '@angular/material';
import { Routes, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';


@NgModule({
    declarations: [
        SubheaderComponent
    ],
    imports: [ 
        RouterModule,
        CommonModule,
        MatButtonModule,
        MatTabsModule,
        MatTableModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
        MatCardModule,
        MatDividerModule,
        MatToolbarModule,
        MatListModule,
        FormsModule,
        MatSnackBarModule
    ],
    exports: [
        RouterModule,
        SubheaderComponent,
        MatButtonModule,
        MatTabsModule,
        MatTableModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
        MatCardModule,
        MatDividerModule,
        MatToolbarModule,
        MatListModule,
        FormsModule,
        MatSnackBarModule
    ],
    providers: []
  })
  export class SharedModule { }