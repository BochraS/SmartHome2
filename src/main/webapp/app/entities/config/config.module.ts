import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CraSharedModule } from 'app/shared';
import {
    ConfigComponent,
    ConfigDetailComponent,
    ConfigUpdateComponent,
    ConfigDeletePopupComponent,
    ConfigDeleteDialogComponent,
    configRoute,
    configPopupRoute
} from './';

const ENTITY_STATES = [...configRoute, ...configPopupRoute];

@NgModule({
    imports: [CraSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [ConfigComponent, ConfigDetailComponent, ConfigUpdateComponent, ConfigDeleteDialogComponent, ConfigDeletePopupComponent],
    entryComponents: [ConfigComponent, ConfigUpdateComponent, ConfigDeleteDialogComponent, ConfigDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    exports: [ ConfigComponent, ConfigDetailComponent, ConfigUpdateComponent, ConfigDeleteDialogComponent, ConfigDeletePopupComponent]
})
export class CraConfigModule {}
