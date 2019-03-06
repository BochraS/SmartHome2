import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CraSharedModule } from 'app/shared';
import { CraAdminModule } from 'app/admin/admin.module';
import {
    ProfileComponent,
    ProfileDetailComponent,
    ProfileUpdateComponent,
    ProfileDeletePopupComponent,
    ProfileDeleteDialogComponent,
    profileRoute,
    profilePopupRoute
} from './';
import { ProfilesListComponent } from './profiles-list.component';

const ENTITY_STATES = [...profileRoute, ...profilePopupRoute];

@NgModule({
    imports: [CraSharedModule, CraAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ProfileComponent,
        ProfileDetailComponent,
        ProfileUpdateComponent,
        ProfileDeleteDialogComponent,
        ProfileDeletePopupComponent,
        ProfilesListComponent
    ],
    exports: [ProfileComponent,
        ProfileDetailComponent,
        ProfileUpdateComponent,
        ProfileDeleteDialogComponent,
        ProfileDeletePopupComponent],
    entryComponents: [ProfileComponent, ProfileUpdateComponent, ProfileDeleteDialogComponent, ProfileDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CraProfileModule {}
