import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CraSharedModule } from 'app/shared';
import {
    ConfValueComponent,
    ConfValueDetailComponent,
    ConfValueUpdateComponent,
    ConfValueDeletePopupComponent,
    ConfValueDeleteDialogComponent,
    confValueRoute,
    confValuePopupRoute
} from './';

const ENTITY_STATES = [...confValueRoute, ...confValuePopupRoute];

@NgModule({
    imports: [CraSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ConfValueComponent,
        ConfValueDetailComponent,
        ConfValueUpdateComponent,
        ConfValueDeleteDialogComponent,
        ConfValueDeletePopupComponent
    ],
    entryComponents: [ConfValueComponent, ConfValueUpdateComponent, ConfValueDeleteDialogComponent, ConfValueDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CraConfValueModule {}
