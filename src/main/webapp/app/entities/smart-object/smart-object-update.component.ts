import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ISmartObject } from 'app/shared/model/smart-object.model';
import { SmartObjectService } from './smart-object.service';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-smart-object-update',
    templateUrl: './smart-object-update.component.html'
})
export class SmartObjectUpdateComponent implements OnInit {
    smartObject: ISmartObject;
    isSaving: boolean;

    users: IUser[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected smartObjectService: SmartObjectService,
        protected userService: UserService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ smartObject }) => {
            this.smartObject = smartObject;
        });
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.smartObject.id !== undefined) {
            this.subscribeToSaveResponse(this.smartObjectService.update(this.smartObject));
        } else {
            this.subscribeToSaveResponse(this.smartObjectService.create(this.smartObject));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ISmartObject>>) {
        result.subscribe((res: HttpResponse<ISmartObject>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }
}
