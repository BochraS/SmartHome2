import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CraSharedModule } from 'app/shared';
import { HOME_ROUTES, HomeComponent } from './';
import {SmartObjectComponent} from 'app/entities/smart-object';
import {SmartObjectUpdateComponent, SmartObjectDetailComponent, SmartObjectDeleteDialogComponent, SmartObjectDeletePopupComponent} from 'app/entities/smart-object';
import {ProfileComponent,
    ProfileDetailComponent,
    ProfileUpdateComponent,
    ProfileDeleteDialogComponent,
    ProfileDeletePopupComponent, ProfilesListComponent} from 'app/entities/profile';
import {CraConfigModule} from "app/entities/config/config.module";

@NgModule({
    imports: [CraSharedModule, RouterModule.forChild(HOME_ROUTES), CraConfigModule],
    declarations: [HomeComponent, SmartObjectComponent, SmartObjectDetailComponent,
        SmartObjectUpdateComponent,
        SmartObjectDeleteDialogComponent,
        SmartObjectDeletePopupComponent, ProfileComponent,
        ProfileDetailComponent,
        ProfileUpdateComponent,
        ProfileDeleteDialogComponent,
        ProfileDeletePopupComponent, ProfilesListComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CraHomeModule {}
