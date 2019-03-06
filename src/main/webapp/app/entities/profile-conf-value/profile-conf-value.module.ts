import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CraSharedModule } from 'app/shared';
import {
    ProfileConfValueComponent,
    ProfileConfValueDetailComponent,
    ProfileConfValueUpdateComponent,
    ProfileConfValueDeletePopupComponent,
    ProfileConfValueDeleteDialogComponent,
    profileConfValueRoute,
    profileConfValuePopupRoute
} from './';

const ENTITY_STATES = [...profileConfValueRoute, ...profileConfValuePopupRoute];

@NgModule({
    imports: [CraSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ProfileConfValueComponent,
        ProfileConfValueDetailComponent,
        ProfileConfValueUpdateComponent,
        ProfileConfValueDeleteDialogComponent,
        ProfileConfValueDeletePopupComponent
    ],
    entryComponents: [
        ProfileConfValueComponent,
        ProfileConfValueUpdateComponent,
        ProfileConfValueDeleteDialogComponent,
        ProfileConfValueDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CraProfileConfValueModule {}
